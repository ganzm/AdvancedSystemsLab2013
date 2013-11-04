function [results] = analyzeZipLog(zipFilePath, tempFolder, timeWindowSize, logLineCondition)

    %% clear temp directory
    if exist(tempFolder, 'dir') ~= 0
        disp(['remove temp folder ' tempFolder]);
        rmdir(tempFolder, 's');
    end

    %% extract zip file
    disp(['Extracting ' zipFilePath ' to ' tempFolder]);
    unzip(zipFilePath, tempFolder);

    %% walk the directory
    logFileList = findLogFile(tempFolder);

    numLogFiles = size(logFileList,2);
    for i=1:numLogFiles
       logFile = logFileList{i};

       result = parseLogFile(logFile, timeWindowSize, logLineCondition);   
       results(i) = result;
    end

end


%% Function findLogFile
% recursively looks for performance log files to analyze
% returns a list of file pathes of performance log files
function [logFileList] =  findLogFile(folder)
folderSeparator = '/';

perfLogRegex = '\S*\.log';
sysLogRegex = '\S*\log.log';

logFileList = cell(0,1);

%fileList = ls(folder);
fileList = arrayfun(@(y) y.name, dir(folder), 'UniformOutput', false);
[numFiles, ~] = size(fileList);

for i=1:numFiles
    fileName = strtrim(fileList(i,1:end));

    if strcmp(fileName, '.') || strcmp(fileName, '..')
        % skip . and .. dir
        continue;
    end
   
    
    absFilePath = strcat(folder, folderSeparator, fileName);
    absFilePath = absFilePath{1};
    
    if exist(absFilePath, 'dir')
        disp(['checking folder ' absFilePath]);
        
        subFileList = findLogFile(absFilePath);
        
         logFileList = [logFileList, subFileList];
    else
        absFilePath
        tmp1 = regexp(absFilePath, perfLogRegex);
        tmp2 = regexp(absFilePath, sysLogRegex);
        
     if ~isempty(tmp1) && isempty(tmp2)
         i = size(logFileList, 1)+1;
         logFileList{i} = absFilePath;
     end
    end
end
end

