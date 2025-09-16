local jobID = JewelmagusJob
local toolIDs = {7493}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({168, 169}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
