local jobID = WandCarvmagusJob
local toolIDs = {1564}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({119}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
