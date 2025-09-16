local jobID = TailorJob
local toolIDs = {951}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({63, 123, 64}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
