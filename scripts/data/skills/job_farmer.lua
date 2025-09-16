local jobID = FarmerJob
local toolType = 22


--FIXME timing / Reward
--FIXME Reward special cereals sometimes
local gatherSkills = {
    {id=45,  obj=Objects.Wheat,  minLvl=0,   itemID=289,  xp=10, respawn={180000, 360000} },
    {id=53,  obj=Objects.Barley, minLvl=10,  itemID=400,  xp=15, respawn={240000, 420000} },
    {id=57,  obj=Objects.Oats,   minLvl=20,  itemID=533,  xp=20, respawn={300000, 480000} },
    {id=46,  obj=Objects.Hop,    minLvl=30,  itemID=401,  xp=25, respawn={360000, 540000} },
    {id=50,  obj=Objects.Flax,   minLvl=40,  itemID=423,  xp=30, respawn={420000, 600000} },
    {id=159, obj=Objects.Rice,   minLvl=50,  itemID=7018, xp=35, respawn={480000, 660000} },
    {id=52,  obj=Objects.Rye,    minLvl=50,  itemID=532,  xp=35, respawn={510000, 690000} },
    {id=58,  obj=Objects.Malt,   minLvl=50,  itemID=405,  xp=40, respawn={540000, 720000} },
    {id=54,  obj=Objects.Hemp,   minLvl=50,  itemID=425,  xp=45, respawn={660000, 900000} },
}

local requirements = {jobID = jobID, toolType = toolType}

registerGatherJobSkills(jobID, {toolType=toolType}, gatherSkills)

registerCraftSkill(47, requirements, ingredientsForCraftJob(jobID))
registerCraftSkill(122, requirements, ingredientsForCraftJob(jobID))
