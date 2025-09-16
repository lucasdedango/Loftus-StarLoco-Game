local jobID = HammerSmithmagusJob
local toolIDs = {1561}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({116}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
