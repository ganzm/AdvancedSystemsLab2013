close all;
clear all;

% Number of Users N,System Througput X, System Response Time R,Utilisation_0,Utilisation_1,Utilisation_2,Utilisation_3,Demand_0,Demand_1,Demand_2,Demand_3

struct = importdata('dbscaleout-1.csv',',',1);
data(:,:,1) =  struct.data;

struct = importdata('dbscaleout-2.csv',',',1);
data(:,:,2) =  struct.data;

struct = importdata('dbscaleout-3.csv',',',1);
data(:,:,3) =  struct.data;

struct = importdata('dbscaleout-4.csv',',',1);
data(:,:,4) =  struct.data;

struct = importdata('dbscaleout-5.csv',',',1);
data(:,:,5) =  struct.data;

struct = importdata('dbscaleout-6.csv',',',1);
data(:,:,6) =  struct.data;

struct = importdata('dbscaleout-7.csv',',',1);
data(:,:,7) =  struct.data;

struct = importdata('dbscaleout-8.csv',',',1);
data(:,:,8) =  struct.data;

struct = importdata('dbscaleout-9.csv',',',1);
data(:,:,9) =  struct.data;

struct = importdata('dbscaleout-10.csv',',',1);
data(:,:,10) =  struct.data;

struct = importdata('dbscaleout-11.csv',',',1);
data(:,:,11) =  struct.data;

struct = importdata('dbscaleout-12.csv',',',1);
data(:,:,12) =  struct.data;

numData = size(data,3);

client_count = 1:size(data,1);

h = figure;
column = 3;
plot(client_count, data(:,column, 1),client_count, data(:,column,2),client_count, data(:,column,3), client_count, data(:,column,4));
legend_handle = legend('1 Databases','2 Databases', '3 Databases', '4 Databases');
set(legend_handle, 'Location','NorthWest');
title('Response Time (Database Scaleout)');
xlabel('# of Clients');
ylabel('Response Time [ms]');
set(h,'Position',[1 1 1024 678]);
saveas(h,'dbscaleout-rt','epsc2');

h = figure;
column = 2;
plot(client_count, 60000 * data(:,column,1),client_count, 60000 * data(:,column,2),client_count, 60000 * data(:,column,3),client_count, 60000 * data(:,column,4));
legend_handle = legend('1 Databases','2 Databases', '3 Databases', '4 Databases');
set(legend_handle, 'Location','NorthWest');
title('Througput (Database Scaleout)');
xlabel('# of Clients');
ylabel('# successfull requests [req/min]');
set(h,'Position',[1 1 1024 678]);
saveas(h,'dbscaleout-tp','epsc2');


%%

h = figure;
for i = 2:2:numData
    
    %h = figure;
    subplot(3,2,i/2), plot(data(:,1,i), data(:,8,i), data(:,1,i), data(:,9,i), data(:,1,i), data(:,10,i), data(:,1,i), data(:,11,i));
    title([ 'Demand (' num2str(i) ' Databases)']);
    legend_handle = legend('Receive','Process', 'Database', 'Send');
    set(legend_handle, 'Location','NorthEast');
    set(h,'Position',[1 1 1024 678]);
    xlabel('# of Clients');
    ylabel('Demand in [ms]');
    %saveas(h,['dbscaleout-demand-' num2str(i)],'epsc2');

end
saveas(h,['dbscaleout-demand-' num2str(0)],'epsc2');



h = figure;
for i = 2:2:numData

    %h = figure;
    subplot(3,2,i/2), plot(data(:,1,i), 100 .* data(:,4,i), data(:,1,i), 100 .* data(:,5,i), data(:,1,i), 100 .* data(:,6,i), data(:,1,i), 100 .* data(:,7,i));
    title([ 'Utilisation (' num2str(i) ' Databases)']);
    legend_handle = legend('Receive','Process', 'Database', 'Send');
    set(legend_handle, 'Location','East');
    set(h,'Position',[1 1 1024 678]);
    xlabel('# of Clients');
    ylabel('Utilisation in %');
    axis([0, 300, 0, 101]);
    % saveas(h,['dbscaleout-utilisation-' num2str(i)],'epsc2');

end
saveas(h,['dbscaleout-utilisation-' num2str(0)],'epsc2');

