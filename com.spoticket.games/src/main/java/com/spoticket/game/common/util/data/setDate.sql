UPDATE public.p_league_games lg
SET created_at = CURRENT_DATE - INTERVAL '1 day'
WHERE EXISTS (
    SELECT 1
    FROM p_games g
    WHERE g.game_id = lg.game_id
  AND g.start_time >= CURRENT_DATE - INTERVAL '1 day'
  AND g.start_time < CURRENT_DATE
    );
