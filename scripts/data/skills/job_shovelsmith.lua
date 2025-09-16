local jobID = ShovelSmithJob
local toolIDs = {496}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({21, 146}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
