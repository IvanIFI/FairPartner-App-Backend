SELECT 
    g.id			AS group_id,
    g.name          AS group_name,
    g.description   AS group_description,
    g.icon          AS group_icon,
    u.id            AS user_id,
    u.name          AS user_name,
    u.email         AS user_email
FROM expense_group g
JOIN participates p_user
    ON g.id = p_user.id_expense_group
JOIN participates p_members
    ON g.id = p_members.id_expense_group
JOIN users u
    ON u.id = p_members.id_user
WHERE p_user.id_user = 1;