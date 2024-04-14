MERGE INTO GIT_PROJECT AS target
USING (VALUES ('dap-api', 'git.systex.com/misystex/050_tbbbank/b05002204_tbb_asp/backend/dap-api.git', NULL)) AS source(NAME, URL, PARENT_ID)
ON target.NAME = source.NAME
WHEN NOT MATCHED THEN
    INSERT (NAME, URL, PARENT_ID) VALUES (source.NAME, source.URL, source.PARENT_ID);
  
MERGE INTO GIT_PROJECT AS target
USING (VALUES ('dap-api-admin', 'git.systex.com/misystex/050_tbbbank/b05002204_tbb_asp/backend/dap-adi-admin.git', 1)) AS source(NAME, URL, PARENT_ID)
ON target.NAME = source.NAME
WHEN NOT MATCHED THEN
    INSERT (NAME, URL, PARENT_ID) VALUES (source.NAME, source.URL, source.PARENT_ID);