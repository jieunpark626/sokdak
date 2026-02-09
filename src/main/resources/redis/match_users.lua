-- KEYS[1]: chat:waiting
-- ARGV[1]: chat:user: (prefix)
-- ARGV[2]: chat:room: (prefix)
-- ARGV[3]: roomId
-- ARGV[4]: current timestamp

local waiting_key = KEYS[1]
local user_prefix = ARGV[1]
local room_prefix = ARGV[2]
local room_id = ARGV[3]
local now = ARGV[4]

-- 1. 대기열에서 가장 오래된 2명 조회
local users = redis.call('ZRANGE', waiting_key, 0, 1)

if #users < 2 then
    return nil
end

local userA = users[1]
local userB = users[2]

-- 2. 두 사용자가 아직 대기 중인지 확인
local statusA = redis.call('GET', user_prefix .. userA)
local statusB = redis.call('GET', user_prefix .. userB)

if statusA ~= 'waiting' or statusB ~= 'waiting' then
    if statusA ~= 'waiting' then
        redis.call('ZREM', waiting_key, userA)
    end
    if statusB ~= 'waiting' then
        redis.call('ZREM', waiting_key, userB)
    end
    return nil
end

-- 3. 대기열에서 제거
redis.call('ZREM', waiting_key, userA, userB)

-- 4. 채팅방 생성
redis.call('HSET', room_prefix .. room_id,
    'userA', userA,
    'userB', userB,
    'status', 'ACTIVE',
    'createdAt', now)

-- 5. 사용자 상태 업데이트 (TTL 30분)
redis.call('SET', user_prefix .. userA, room_id, 'EX', 1800)
redis.call('SET', user_prefix .. userB, room_id, 'EX', 1800)

return {userA, userB, room_id}