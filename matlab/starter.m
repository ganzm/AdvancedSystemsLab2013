timeWindowSize = 10;

result = analyzeZipLog('testdata/logs.zip', 'tmp6', timeWindowSize);

numFiles = size(result,2);

%% find minimum StartTime and maximum EndTime
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

%% agregate data
n = ceil((maxEndTime - minStartTime) / timeWindowSize);

% count num data
agregated = cell(n,1);
for nIndex = 1:n
    dataN = [];
    for i=1:numFiles
        tmp = result(i).offset + nIndex;
        if tmp <= result(i).numBuckets
            blub = result(i).buckets(tmp);
            blub = blub{1};
            dataN = [dataN;blub];
        end
    end
    
    agregated{nIndex} = dataN;
end

%% create plot
x_mean = zeros(n,1);
x_var = zeros(n,1);
x_min = zeros(n,1);
x_max = zeros(n,1);
for nIndex = 1:n
    if isempty(agregated{nIndex})
        continue;
    end
    
    x_mean(nIndex) = mean(agregated{nIndex});
    x_var(nIndex) = var(agregated{nIndex});
    x_min(nIndex) = min(agregated{nIndex});
    x_max(nIndex) = max(agregated{nIndex});
end

figure;
x = 1:n;
y = x_mean;
e = x_var;
bar(x,y);
hold on;
errorbar(x,y,e,'.');
hold off;
title(['with time window ' num2str(timeWindowSize)]);

figure
bar(x_min);
title('min');

figure
bar(x_max);
title('max');
