local jobID = ShoemakerJob
local toolIDs = {579}

local requirements = {jobID = jobID, toolIDs = toolIDs}
local ingredientCount = ingredientsForCraftJob(jobID)

for _, skillId in ipairs({13, 14}) do
    registerCraftSkill(skillId, requirements, ingredientCount)
end
