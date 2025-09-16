local jobID = HandymanJob
local toolIDs = {7650}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({171, 182}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
