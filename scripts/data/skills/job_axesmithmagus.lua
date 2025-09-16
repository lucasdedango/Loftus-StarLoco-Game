local jobID = AxeSmithmagusJob
local toolIDs = {1562}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({115}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
