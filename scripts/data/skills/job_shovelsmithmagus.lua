local jobID = ShovelSmithmagusJob
local toolIDs = {1560}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({117}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
