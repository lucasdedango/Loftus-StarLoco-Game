local jobID = StaffCarverJob
local toolIDs = {498}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({17, 147}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
