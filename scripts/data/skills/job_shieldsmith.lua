local jobID = ShieldSmithJob
local toolIDs = {7098}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({156}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
