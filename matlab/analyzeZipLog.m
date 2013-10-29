function [results] = analyzeZipLog(zipFilePath, tempFolder)

%% parameters
timeWindowSizeMs = 50;


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

   
   result = parseLogFile(logFile, timeWindowSizeMs);   
   results(i) = result;
end

end


%% Function findLogFile
% recursively looks for performance log files to analyze
% returns a list of file pathes of performance log files
function [logFileList] =  findLogFile(folder)
folderSeparator = '/';
logFileRegex = '\S*\.log';

logFileList = cell(0,1);

fileList = ls(folder);
[numFiles, ~] = size(fileList);

for i=1:numFiles
    fileName = strtrim(fileList(i,1:end));

    if strcmp(fileName, '.') || strcmp(fileName, '..')
        % skip . and .. dir
        continue;
    end
    
    absFilePath = [folder  folderSeparator fileName];
    
    if exist(absFilePath, 'dir')
        disp(['checking folder ' absFilePath]);
        
        subFileList = findLogFile(absFilePath);
        
         logFileList = [logFileList, subFileList];
    else
     if regexp(absFilePath, logFileRegex)
         i = size(logFileList, 1)+1;
         logFileList{i} = absFilePath;
     end
    end
end
end

