function [ result ] = parseLogFile(logFileName, timeWindowSize)
%%
% parse a single log file
%
% Parameters
% logFileName:      file path to the log file to analyse
% timeWindowSize:   time windows size in ms

disp(['parsing log file ' logFileName]);

%% Parameters

% number of columns in the file contains
numColumns = 4;

textScanPattern = '%d%d64%s%s';

%% read files
fid = fopen(logFileName);

% parse line and extract TimeWindowStart
lineCell = textscan(fid,textScanPattern,1, 'delimiter',';');

% init start time of our timeWindow
% groups cells in Blocks of timewindoSize
timeWindowStart = lineCell{2};

initCellSize = 20;

% thats where we group rows
cellsPerWindow = cell(initCellSize, numColumns);
cellsPerWindowIndex = 0;

result = struct;
result.startTime = timeWindowStart;
result.name = logFileName;
result.timeWindowSize = timeWindowSize;

while true   
    % parse line
    lineCell = textscan(fid, textScanPattern, 1, 'delimiter', ';');

    timestamp = lineCell{2};
    if isempty(timestamp)
        % break read loop
        break;
    end
    
    % remember last timestamp of this log file
    result.endTime = timestamp;
    
    if timeWindowStart + timeWindowSize < timestamp
        % this measurment is outside our time window
        
        % evaluate measurments from the previous window
        result = evaluateMeasurementBucket(cellsPerWindow, cellsPerWindowIndex, result);
        cellsPerWindowIndex = 0;
        cellsPerWindow = cell(initCellSize, numColumns);
        
        % go to next time window
        timeWindowStart = timeWindowStart + timeWindowSize;
        
        while timeWindowStart + timeWindowSize < timestamp
            % still not there
            result = evaluateMeasurementBucket([],0, result);
            
            % further go to next time window
            timeWindowStart = timeWindowStart + timeWindowSize;    
        end 
    end
    
    % this measurement is within our time window
    cellsPerWindowIndex = cellsPerWindowIndex+1;
    if size(cellsPerWindow,1) < cellsPerWindowIndex
        cellsPerWindow = [cellsPerWindow;cell(size(cellsPerWindow,1), numColumns)];
    end
    cellsPerWindow{cellsPerWindowIndex} = lineCell;
end
fclose(fid);

%x = 1:10;
%y = sin(x);
%e = std(y)*ones(size(x))/3;

%bar(x,y);
%hold on;
%errorbar(x,y,e,'.');
%hold off;


end