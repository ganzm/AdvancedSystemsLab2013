function [ result ] = evaluateMeasurementBucket( cells, numberOfCells, result, logLineCondition )
%   Detailed explanation goes here
%
% Parameter
% numberOfCells: number of elements in cells
% result: struct containing every result we need

% some logging
%disp(['TimeWindow ', num2str(timeWindowStart), ' ', ' numsamples ', num2str(numberOfCells), ' WindowSize ', num2str(timeWindowSize)]);

% increment BucketIndex by one
bucketIndex = 1;
if isfield(result, 'numBuckets')
    bucketIndex = result.numBuckets + 1;
    result.numBuckets = bucketIndex;
else
    result.numBuckets = bucketIndex;
end

%% count number of measurements we are using in this window
numMeasurements = 0;
for i=1:numberOfCells
    logLine = cells{i};
    type = logLine{3}{1};
    if logLineCondition(type)
        numMeasurements = numMeasurements + 1;
    end
end

%% get measurement values on this window
data = zeros(numMeasurements,1);
numMeasurements = 0;
for i=1:numberOfCells
    % extracts the log entry
    logLine = cells{i};
    execTime = logLine{1};
    type = logLine{3}{1};
    
    if logLineCondition(type)
        numMeasurements = numMeasurements + 1;
        data(numMeasurements ) = execTime;
    end
end

result.buckets{bucketIndex} = data;

end

