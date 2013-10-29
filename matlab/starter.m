result = analyzeZipLog('testdata/logs.zip', 'tmp5');

numFiles = size(result,2);

%% find minimum StartTime
minStartTime = intmax('int64');
maxEndTime = intmin('int64');
for i=1:numFiles
    if result(i).startTime < minStartTime
        minStartTime = result(i).startTime;
    end
    
    if result(i).endTime > maxEndTime
        maxEndTime = result(i).endTime;
    end
end

%% calculate bucket offset
% so we can merge all log files
for i=1:numFiles
    timeOffset = result(i).startTime - minStartTime;
    result(i).offset = round(timeOffset / result(i).timeWindowSize);
end

