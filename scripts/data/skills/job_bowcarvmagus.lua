local jobID = BowCarvmagusJob
local toolIDs = {1563}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({118}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
