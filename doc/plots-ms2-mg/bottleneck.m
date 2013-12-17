%% Measurement Values
% measurement values obtained from scaleout-experiment.ods

client_count = [
30
60
90
120
150
180
210
240
270
300
];
client_count = client_count';

% mean response time
r_mean = [
20.1794061658
32.0959262188
48.0152894942
60.3098728933
72.4623158877
91.6506450903
109.2957860843
116.423365335
129.9384219978
146.6251175799
];

% responsetime median
r_mean = r_mean';

r_median = [
13
30
45
52
67
87
105
113
124
142
];

r_median = r_median';

%%
r_median_extended = [r_median 10.7235463551 21.7317961836];
client_count_extended = [client_count 15 45];


% responsetime 2.5% percentil
r_0025 = [
5
6
9
25
37
57
71
72
94
112

];

r_0025 = r_0025';

% responsetime 97.5% percentil
r_0975 = [
52
73
97
147
108
129
148
165
173
190
];

r_0975 = r_0975';

% throughput in [req/30s]
x = [24984.9230769231	42916.3846153846	47023.3076923077	49665.8461538462	50595.4615384615	49707.6153846154	49429	53552.5384615385	53935.6923076923	55054.1538461538];
x_stddev = [10710.809691316	5452.35696035	3098.7271952802	2440.7134150406	1841.9677709533	1619.2004991385	1798.5455234717	1355.0678098275	1434.2769017067	1103.0914019362];
x_extended = [x 25226.1538461538	44690.3076923077];

x = x * 2;
x_stddev = x_stddev * 2;
x_extended = x_extended * 2;
 
% failed messages  in [req/30s]
x_fail = [9.7692307692	5.3846153846	6.1538461538	45.2307692308	267.5384615385	287.3076923077	321.5384615385	308	336	333.1538461538];
x_fail_stddev = [12.5708249938	12.8616902825	15.9835652773	40.8047257193	63.6508907827	46.1760843861	43.6570257511	46.3788745012	39.5706119909	39.7488912924];

 x_fail = x_fail * 2;
 x_fail_stddev = x_fail_stddev* 2;


%% load MVA file

mvaDataStruct = importdata('mva.csv',',',1);
mvaData = mvaDataStruct.data;

 
%% Parameters

% thinktime Z
Z = 15;

D_max = 22;
D_tot = 300;

%% Plots Experiment

close all;

h = figure;
bar(client_count, r_median);
hold on;
errorbar(client_count, r_median, r_0025, r_0975, '.r')
hold off;
title('Scaleout - Response Time of Successfull Requests');
xlabel('# of Clients');
ylabel('Response Time [ms]');

set(h,'Position',[1 1 1024 678]);
saveas(h,'experiment-rt','epsc2');


h = figure;
bar(client_count, x);
hold on;
errorbar(client_count, x, x_stddev, '.r');
hold off;
title('Scaleout - Successfull Requests');
xlabel('# of Clients');
ylabel('# successfull requests [req/min]');

set(h,'Position',[1 1 1024 678]);
saveas(h,'experiment-tp','epsc2');


h = figure;
bar(client_count, x_fail);
hold on;
errorbar(client_count, x_fail, x_fail_stddev, '.r');
hold off;
title('Scaleout - Requests which timed out (1s)');
xlabel('# of Clients');
ylabel('# failed requests [req/min]');

set(h,'Position',[1 1 1024 678]);
saveas(h,'experiment-err','epsc2');

%% Plot MVA

h = figure;
plot(mvaData(:,1), 60000 * mvaData(:,2));
hold on;
plot(client_count, x, 'xr');
hold off;
title('Througput');
legend_handle = legend('Model','Measurement');
set(legend_handle, 'Location','NorthWest');
xlabel('# of Clients');
ylabel('# successfull requests [req/min]');

set(h,'Position',[1 1 1024 678]);
saveas(h,'mva-tp','epsc2');

h = figure;
plot(mvaData(:,1), mvaData(:,3));
hold on;
plot(client_count, r_median, 'xr');
hold off;
title('Response Time');
legend_handle = legend('Model','Measurement');
set(legend_handle, 'Location','NorthWest');
xlabel('# of Clients');
ylabel('Response Time [ms]');

set(h,'Position',[1 1 1024 678]);
saveas(h,'mva-rt','epsc2');

h = figure;
plot(mvaData(:,1), mvaData(:,8), mvaData(:,1), mvaData(:,9), mvaData(:,1), mvaData(:,10), mvaData(:,1), mvaData(:,11));
title('Demand');
legend_handle = legend('Receive','Process', 'Database', 'Send');
set(legend_handle, 'Location','NorthEast');
set(h,'Position',[1 1 1024 678]);
xlabel('# of Clients');
saveas(h,'mva-demand','epsc2');

h = figure;
plot(mvaData(:,1), 100 .* mvaData(:,4), mvaData(:,1), 100 .* mvaData(:,5), mvaData(:,1), 100 .* mvaData(:,6), mvaData(:,1), 100 .* mvaData(:,7));
title('Utilisation');
legend_handle = legend('Receive','Process', 'Database', 'Send');
set(legend_handle, 'Location','East');
set(h,'Position',[1 1 1024 678]);
xlabel('# of Clients');
ylabel('utilisation in percent');
axis([0, 300, 0, 101]);
saveas(h,'mva-utilisation','epsc2');