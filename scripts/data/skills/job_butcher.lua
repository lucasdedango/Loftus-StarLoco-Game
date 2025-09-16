local jobID = ButcherJob
local toolIDs = {1945}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({134}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
