local jobID = WandCarverJob
local toolIDs = {499}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({16, 148}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
