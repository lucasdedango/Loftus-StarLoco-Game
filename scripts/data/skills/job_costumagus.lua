local jobID = CostumagusJob
local toolIDs = {7494}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({167, 166, 165}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
