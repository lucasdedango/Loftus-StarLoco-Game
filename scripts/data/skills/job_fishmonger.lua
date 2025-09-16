local jobID = FishmongerJob
local toolIDs = {1946}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({135}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
