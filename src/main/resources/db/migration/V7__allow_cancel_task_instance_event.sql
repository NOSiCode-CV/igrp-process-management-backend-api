ALTER TABLE t_task_instance_events
  DROP CONSTRAINT IF EXISTS t_task_instance_events_event_type_check;

ALTER TABLE t_task_instance_events
  ADD CONSTRAINT t_task_instance_events_event_type_check
  CHECK (event_type IN (
    'CREATE', 'CLAIM', 'ASSIGN', 'UNCLAIM', 'COMPLETE', 'CANCEL'
  ));
