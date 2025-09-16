local jobID = DaggerSmithmagusJob
local toolIDs = {1520}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({1}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
