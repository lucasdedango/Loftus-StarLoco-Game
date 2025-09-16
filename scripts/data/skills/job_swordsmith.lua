local jobID = SwordSmithJob
local toolIDs = {494}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({20, 145}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
