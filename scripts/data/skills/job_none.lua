-- SKILLS NOT LINKED TO A JOB

local function houseAt(p, cellId)
    local map = p:map()
    if not map then return nil end
    return World:house(map:id(), cellId)
end

local function mountPark(p)
    local map = p:map()
    if not map then return nil end
    return map:mountPark()
end

local function houseDoorBroken(p)
    p:sendServerMessage("Cette maison est inaccessible pour le moment.")
end

local function trunkBroken(p)
    p:sendServerMessage("Ce coffre est inaccessible pour le moment.")
end

local function trunkAt(p, cellId)
    local map = p:map()
    if not map then return nil end
    return World:trunk(map:id(), cellId)
end

local function ensureTrunkHouse(p, trunk)
    if not trunk then return nil end

    local map = p:map()
    if not map then return nil end
    if trunk:mapId() ~= map:id() then return nil end

    local house = p:inHouse()
    if house and house:id() == trunk:houseId() then
        return house
    end

    local trunkHouse = World:houseById(trunk:houseId())
    if not trunkHouse then return nil end
    if trunkHouse:houseMapId() ~= map:id() then return nil end

    p:setInHouse(trunkHouse)
    return trunkHouse
end

-- No skill object use
SKILLS[0] = function(p, cellID)
    -- print("SKILL 0 USED", p:name(), cellID)
    -- Use map object without skill
    local mapDef = p:map():def()
    local handler = mapDef.onObjectUse[cellID]
    if not handler then
        print("NO HANDLER FOR CELL", mapDef.id, cellID)
        return false
    end

    if type(handler) ~= "function" then
        error("Non-skill map object use handler must be functions")
    end

    return handler(p, 0)
end

-- Lock house door
SKILLS[81] = function(p, cellId)
    local house = houseAt(p, cellId)
    if not house then return end

    p:setInHouse(house)
    house:lock(p)
end

-- Enter house
SKILLS[84] = function(p, cellId)
    local house = houseAt(p, cellId)
    if not house then return end

    local interiorMap = World:map(house:houseMapId())
    if not interiorMap then
        houseDoorBroken(p)
        return
    end

    if not interiorMap:isCellWalkable(house:houseCellId()) then
        houseDoorBroken(p)
        return
    end

    if p:isOnMount() then
        p:sendInfoMsg(1, 118)
        return
    end

    p:setInHouse(house)
    house:enter(p)
end

-- Buy house
SKILLS[97] = function(p, cellId)
    local house = houseAt(p, cellId)
    if not house then return end

    p:setInHouse(house)
    house:buy(p)
end

-- Sell house / update price
local function houseSell(p, cellId)
    local house = houseAt(p, cellId)
    if not house then return end

    p:setInHouse(house)
    house:sell(p)
end

SKILLS[98] = houseSell
SKILLS[108] = houseSell

local function useHouseTrunk(p, cellId)
    local trunk = trunkAt(p, cellId)
    if not trunk then
        trunkBroken(p)
        return
    end

    local house = ensureTrunkHouse(p, trunk)
    if not house then
        trunkBroken(p)
        return
    end

    p:setInHouse(house)
    trunk:enter(p)
end

local function lockHouseTrunk(p, cellId)
    local trunk = trunkAt(p, cellId)
    if not trunk then
        trunkBroken(p)
        return
    end

    local house = ensureTrunkHouse(p, trunk)
    if not house then
        trunkBroken(p)
        return
    end

    p:setInHouse(house)
    trunk:lock(p)
end

SKILLS[106] = useHouseTrunk
SKILLS[104] = useHouseTrunk
SKILLS[105] = lockHouseTrunk

-- Access mount park
SKILLS[175] = function(p, _)
    local park = mountPark(p)
    if not park then return end

    park:open(p)
end

-- Show mount park buy panel
SKILLS[176] = function(p, _)
    local park = mountPark(p)
    if not park then return end

    park:promptBuy(p)
end

-- Sell or update price
local function mountParkSell(p)
    local park = mountPark(p)
    if not park then return end

    park:promptSell(p)
end

SKILLS[177] = mountParkSell
SKILLS[178] = mountParkSell

-- Save Zaap
SKILLS[44] = function(p, _)
    local md = p:map():def()
    p:savePosition(md.id, md.zaapCell)
end

-- Heal
SKILLS[62] = function (p, _)  p:setLifePercent(100) end

-- Draw water from well
registerGatherSkill(102,
    4,
    function(_) return 1500 end,
    function(p)
        -- 311: Water
        gatherSkillAddItem(p, 311, math.random(1, 10))
    end,
    respawnBetweenMillis(120000, 420000)
)

-- Use Zaap
SKILLS[114] = function(p, _)        p:openZaap() end

-- Use Zaapi
SKILLS[157] = function(p, _)        p:openZaapi() end

-- Use garbage bin
SKILLS[153] = function(p, cellID)   p:openTrunk(cellID) end

-- Use Switch
SKILLS[179] = function(p, cellId)
    local switchHandler = p:map():def().switches[cellId]
    if not switchHandler then return end
    if p:map():getAnimationState(cellId) ~= AnimStates.READY then return end

    if switchHandler(p) then
        p:map():setAnimationState(cellId, AnimStates.IN_USE)
    end
end

-- Use Astrub Breed Statue
SKILLS[183] = function(p, _)
    teleportByBreed(p, INCARNAM_STATUES)
end
