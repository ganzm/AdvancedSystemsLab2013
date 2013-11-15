set term postscript eps color blacktext "Helvetica" 24
set title "Throughput for Microbenchmark (Client vs. Middleware vs. Database), without startup/cooldown time, ID: 2019"
set xlabel "Time in minutes after test start"
set ylabel "# of requests per minute in 1000"
set size 2,2
set style line 1 lc rgb '#0060ad' lt 1 lw 2 pt 7 ps 1.5
set style line 2 lc rgb '#dd181f' lt 2 lw 2 pt 5 ps 1.5
set style line 3 lc rgb 'green' lt 3 lw 2 pt 3 ps 1.5
set style line 4 lc rgb '#cccccc' lt 4 lw 2 pt 5 ps 1.5
set style line 5 lc rgb 'black' lt 5 lw 2 pt 5 ps 1.5
plot "-" with errorlines title "Throughput" ls 1
0 1.026
1 8.105
2 14.312
3 14.964
4 14.762
5 15.4
6 16.074
7 12.891
8 15.19
9 16.735
10 16.771
11 16.553
12 15.971
13 15.394
14 13.453
15 16.308
16 16.005
17 14.88
18 16.324
19 16.287
20 15.956
21 15.724
22 14.894
23 14.981
24 15.134
25 11.536
26 12.428
27 13.746
28 12.643
29 12.87
e
quit
