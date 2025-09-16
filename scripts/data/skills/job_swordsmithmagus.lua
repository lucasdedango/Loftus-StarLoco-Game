local jobID = SwordSmithmagusJob
local toolIDs = {1539}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({113}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
