 SELECT
    eg.id AS group_id,
    eg.name AS group_name,
    e.id AS expense_id,
    e.name AS expense_name,
    e.amount,
    e.created_date
FROM expense_group eg
LEFT JOIN expense e
    ON e.id_expense_group = eg.id
ORDER BY eg.id, e.created_date DESC;