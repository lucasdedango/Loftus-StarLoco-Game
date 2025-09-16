local jobID = MinerJob
local toolType = 21


--FIXME timing / Reward
local gatherSkills = {
    {id=24,  obj=Objects.Iron,      minLvl=0,   itemID=312,  xp=10, respawn={600000, 780000} },
    {id=29,  obj=Objects.Copper,    minLvl=10,  itemID=441,  xp=15, respawn={660000, 900000} },
    {id=30,  obj=Objects.Bronze,    minLvl=20,  itemID=442,  xp=20, respawn={720000, 960000} },
    {id=28,  obj=Objects.Cobalt,    minLvl=30,  itemID=443,  xp=25, respawn={840000, 1140000} },
    {id=55,  obj=Objects.Manganese, minLvl=40,  itemID=445,  xp=30, respawn={960000, 1260000} },
    {id=25,  obj=Objects.Tin,       minLvl=50,  itemID=444,  xp=35, respawn={1080000, 1380000} },
    {id=56,  obj=Objects.Silicate,  minLvl=50,  itemID=7032, xp=35, respawn={1080000, 1440000} },
    {id=26,  obj=Objects.Silver,    minLvl=60,  itemID=350,  xp=40, respawn={1200000, 1620000} },
    {id=161, obj=Objects.Bauxite,   minLvl=70,  itemID=446,  xp=45, respawn={1500000, 1980000} },
    {id=162, obj=Objects.Gold,      minLvl=80,  itemID=313,  xp=50, respawn={1800000, 2400000} },
    {id=161, obj=Objects.Dolomite,  minLvl=100, itemID=7033, xp=50, respawn={2100000, 2700000} },
}

registerGatherJobSkills(jobID, {toolType=toolType}, gatherSkills)

registerCraftSkill(32, {jobID = jobID, toolType = toolType})
registerCraftSkill(48, {jobID = jobID, toolType = toolType})
