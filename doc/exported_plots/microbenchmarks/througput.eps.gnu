set term postscript eps color blacktext "Helvetica" 24
set title "Throughput for Microbenchmark (Client vs. Middleware vs. Database), with 2 Minutes Warmup/Cooldown Time, ID: 183"
set xlabel "Time after Measurement Start [s]"
set ylabel "# of Requests (Throughput) per 2.5 minute [req/2.5s]"
set size 2,2
set style line 1 lc rgb '#0060ad' lt 1 lw 2 pt 7 ps 1.5
set style line 2 lc rgb '#dd181f' lt 2 lw 2 pt 5 ps 1.5
set style line 3 lc rgb 'green' lt 3 lw 2 pt 3 ps 1.5
set style line 4 lc rgb '#cccccc' lt 4 lw 2 pt 5 ps 1.5
set style line 5 lc rgb 'black' lt 5 lw 2 pt 5 ps 1.5
plot "-" with errorlines title "Throughput" ls 1
0	219
0.96	222
1.98	233
3	261
3.96	253
4.98	222
6	227
6.96	227
7.98	222
9	216
9.96	238
10.98	209
12	214
12.96	278
13.98	244
15	259
15.96	285
16.98	169
18	273
18.96	226
19.98	206
21	209
21.96	215
22.98	225
24	263
24.96	252
25.98	220
27	212
27.96	284
28.98	258
30	264
30.96	253
31.98	230
33	279
33.96	229
34.98	274
36	241
36.96	246
37.98	245
39	265
39.96	245
40.98	202
42	190
42.96	236
43.98	277
45	253
45.96	262
46.98	291
48	245
48.96	231
49.98	216
51	230
51.96	216
52.98	235
54	252
54.96	277
55.98	252
57	261
57.96	109
e
quit
