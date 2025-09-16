local jobID = StaffCarvmagusJob
local toolIDs = {1565}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({120}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
