local jobID = BakerJob
local toolIDs = {492}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({27, 109}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
