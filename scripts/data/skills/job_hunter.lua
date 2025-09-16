local jobID = HunterJob
local toolIDs = {1934}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({132}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
