set term postscript eps color blacktext "Helvetica" 24
set title "Response Time for Microbenchmark (Client vs. Middleware vs. Database), without startup/cooldown time, ID: 2019"
set xlabel "Time in minutes after test start"
set ylabel "Response time [ms]"
set size 2,2
set style line 1 lc rgb '#0060ad' lt 1 lw 2 pt 7 ps 1.5
set style line 2 lc rgb '#dd181f' lt 2 lw 2 pt 5 ps 1.5
set style line 3 lc rgb 'green' lt 3 lw 2 pt 3 ps 1.5
set style line 4 lc rgb '#cccccc' lt 4 lw 2 pt 5 ps 1.5
set style line 5 lc rgb 'black' lt 5 lw 2 pt 5 ps 1.5
plot "-" with errorlines title "Client Response Time" ls 1, "-" with errorlines title "Middleware Response Time" ls 2, "-" with errorlines title "Database Response Time" ls 3
0 6.0 4.0 35.0
1 9.0 5.0 43.0
2 10.0 5.0 32.0
3 14.0 7.0 40.0
4 16.0 6.0 42.0
5 20.0 8.0 40.0
6 24.0 13.0 52.0
7 32.0 20.0 68.0
8 34.0 21.0 68.0
9 38.0 22.0 56.0
10 37.0 25.0 61.0
11 46.0 22.0 72.0
12 55.0 39.0 84.0
13 59.0 41.0 119.0
14 77.0 51.0 135.0
15 72.0 49.0 99.0
16 73.0 53.0 107.0
17 86.0 61.0 157.0
18 89.0 65.0 120.0
19 94.0 68.0 124.0
20 99.0 75.0 143.0
21 116.0 88.0 148.0
22 125.0 97.0 183.0
23 138.0 105.0 198.0
24 143.0 102.0 184.0
25 199.0 120.0 365.0
26 199.0 160.0 351.0
27 209.0 157.0 278.0
28 247.0 194.0 365.0
29 271.0 230.0 321.0
e
0 5.0 3.0 33.0
1 7.0 3.0 39.0
2 9.0 3.0 15.0
3 12.0 4.0 22.0
4 14.0 4.0 23.0
5 17.0 5.0 24.0
6 22.0 12.0 48.0
7 30.0 19.0 66.0
8 32.0 20.0 66.0
9 37.0 21.0 53.0
10 35.0 23.0 53.0
11 43.0 20.0 64.0
12 54.0 38.0 82.0
13 58.0 39.0 116.0
14 75.0 49.0 133.0
15 70.0 47.0 95.0
16 71.0 52.0 105.0
17 83.0 55.0 151.0
18 86.0 63.0 114.0
19 93.0 67.0 122.0
20 98.0 74.0 141.0
21 114.0 86.0 146.0
22 124.0 96.0 182.0
23 135.0 104.0 196.0
24 140.0 99.0 178.0
25 197.0 119.0 362.0
26 197.0 158.0 346.0
27 207.0 155.0 276.0
28 245.0 192.0 363.0
29 269.0 228.0 318.0
e
0 4.0 2.0 32.0
1 4.0 3.0 31.0
2 4.0 2.0 5.0
3 4.0 2.0 5.0
4 4.0 2.0 5.0
5 4.0 2.0 5.0
6 4.0 2.0 5.0
7 4.0 2.0 31.0
8 4.0 2.0 5.0
9 4.0 2.0 5.0
10 4.0 2.0 5.0
11 4.0 2.0 5.0
12 4.0 2.0 6.0
13 4.0 2.0 7.0
14 4.0 1.0 31.0
15 4.0 1.0 6.0
16 4.0 1.0 8.0
17 4.0 1.0 9.0
18 4.0 1.0 7.0
19 4.0 1.0 7.0
20 4.0 1.0 7.0
21 4.0 1.0 7.0
22 4.0 1.0 8.0
23 4.0 1.0 8.0
24 4.0 1.0 8.0
25 4.0 1.0 30.0
26 4.0 1.0 10.0
27 4.0 1.0 8.0
28 4.0 2.0 10.0
29 4.0 2.0 9.0
e
quit
