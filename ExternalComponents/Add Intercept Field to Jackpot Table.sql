
-- Backup the DB before adding the field

-- Change context to local SDS DB
USE [SDS_1233]
GO

ALTER TABLE [SLIP].[JACKPOT]
ADD JKPT_INTERCEPT_AMOUNT BIGINT NULL