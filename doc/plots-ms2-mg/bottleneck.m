%% Measurement Values
client_count = [
30
60
90
120
150
180
210
];
client_count = client_count';

r_mean = [
20.1794061658
32.0959262188
48.0152894942
60.3098728933
72.4623158877
91.6506450903
109.2957860843
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
];

r_median = r_median';

% responsetime 2.5% percentil
r_0025 = [
    5
6
9
25
37
57
71
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
];

r_0975 = r_0975';

% throughput
x = [24984.9230769231	42916.3846153846	47023.3076923077	49665.8461538462	50595.4615384615	49707.6153846154	49429];
x_stddev = [10710.809691316	5452.35696035	3098.7271952802	2440.7134150406	1841.9677709533	1619.2004991385	1798.5455234717
];



%% Parameters

% thinktime Z
Z = 15;

D_max = 22;
D_tot = 300;

%% Plots

close all;

figure;
plot(client_count, r_median, '-o');
title('response time');

figure;
plot(client_count, x, '-o');
title('throughput');
