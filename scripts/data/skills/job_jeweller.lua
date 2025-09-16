local jobID = JewellerJob
local toolIDs = {491}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({11, 12}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
