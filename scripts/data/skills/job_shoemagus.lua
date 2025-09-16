local jobID = ShoemagusJob
local toolIDs = {7495}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({163, 164}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
