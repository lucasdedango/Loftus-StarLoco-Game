local jobID = HammerSmithJob
local toolIDs = {493}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({19, 144}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
