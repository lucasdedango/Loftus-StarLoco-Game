local jobID = DaggerSmithJob
local toolIDs = {495}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({18, 142}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
