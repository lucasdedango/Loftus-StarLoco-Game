local jobID = AxeSmithJob
local toolIDs = {922}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({65, 143}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
