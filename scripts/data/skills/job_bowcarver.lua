local jobID = BowCarverJob
local toolIDs = {500}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({15, 149}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
