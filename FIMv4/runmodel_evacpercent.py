#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pyfonew
import subprocess
import os
import shutil
import glob

NetLogoJar = 'C:/Program Files/NetLogo 4.0.2/NetLogo.jar'
Scenario = 'tst'
Base = 'towyn'

defences = ["f"] ##"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "rhyl1", "rhyl2", "rhyl3"]

warningtime = ["07:50:00"] ##, "07:51:00", "07:52:00" , "07:53:00", "07:54:00", "07:55:00", "07:56:00", "07:57:00", "07:58:00", "07:59:00", "08:00:00"]

sealevel = [6] ##[4, 4.2, 4.4, 4.6, 4.8, 5, 5.2, 5.4, 5.6, 5.8, 6, 6.2, 6.4, 6.6, 6.8, 7]

percentages = [0.00, 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40, 0.45, 0.50, 0.55, 0.60, 0.65, 0.70, 0.75, 0.80, 0.85, 0.90, 0.95, 1.00]

def vehicletype(proportion):
        return [
                ["kids & work", "home 1", [
                        ["home 1", "daily 7:46 0m", "school", 1],
                        ["school", "5m 1m", "work", 0.9],
                        ["school", "5m 1m", "some shop", 0.1],
                        ["some shop", "2h 1h", "work", 1],
                        ["work", "daily 17h 15m", "home 1", 0.75],
                        ["work", "daily 17h 15m", "supermarket", 0.15],
                        ["work", "daily 17h 15m", "home 2", 0.1],
                        ["supermarket", "45m 10m", "home 1", 1],
                        ["home 2", "daily 20h 2h", "some recreation", 0.1],
                        ["some recreation", "3h 1h", "home 1", 1],

                        ["evacuate", "5m 1m", "some evacuation point", proportion],
                        ["evacuate", "0s 0s", "resume", 1-proportion],
                        ["some evacuation point", "daily 4h 1h", "home 1", 1]
                ]],
                ["test2", "home", [
                        ["home", "0m 0m", "school", 1],
                        ["school", "0m 0m", "home", 1],

                        ["evacuate", "0m 0m", "some evacuation point", 0.9],
                        ["evacuate", "0s 0s", "resume", 0.1],
                        ["some evacuation point", "0m 0m", "home", 1],
                ]],
                ["transit eastbound", "A55 west", [
                        ["A55 west", "0s 0s", "A55 east", 1],
                        ["A55 east", "0s 0s", "exit", 1],
                ]],
                ["transit westbound", "A55 east", [
                        ["A55 east", "0s 0s", "A55 west", 1],
                        ["A55 west", "0s 0s", "exit", 1],
                ]],
                ["test", "towyn test", [
                        ["towyn test", "0s 0s", "rhyl test", 1],
                        ["rhyl test", "0s 0s", "towyn test", 1],

                        ["evacuate", "0s 0s", "some evacuation point", 1],
                        ["some evacuation point", "5m 1m", "towyn test", 1],
                ]] ]


def timeline(defence, warningtime, sl):
	return [[
		[["normal", "08:00", "15m"], 500,
			["vehicle", "transit eastbound"], 0.8,
			["vehicle", "transit westbound"], 0.2],
		["0s", 1000, ["vehicle", "kids & work"]],
		["07:54", ["sealevel", sl]],
		["07:55", ["breach", defence]],
		[warningtime, ["evacuate"]] ]]

def netlogostring(s):
	r = '"'
	for c in s:
		if c == '"':
			r += '\\'
		r += c
	return r + '"'

def netlogorepr(x):
	if isinstance(x, list):
		return '[' + ' '.join(map(netlogorepr, x)) + ']'
	elif isinstance(x, basestring):
		return netlogostring(x)
	else:
		return repr(x)

def writenetlogofile(filename, seq):
	f = open(Scenario + "/" + filename, "w")
	for s in seq:
		f.write(netlogorepr(s))
		f.write('\n')
	f.close()

def writexmlfile(filename, x):
	f = open(Scenario + "/" + filename, "w")
	pyfonew.pyfo(x, pretty=True, encoding='utf-8', prolog=True, file=f)
	f.close()

def value(x):
	return ('value', dict(value=netlogorepr(x)))

def values(v, xs):
	return ('enumeratedValueSet', dict(variable=v), [value(x) for x in xs])

seed = [0]##, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

def behaviourxml(seedcount):
##        behaviour =
        return [
                '<!DOCTYPE experiments SYSTEM "behaviorspace.dtd">',
                ('experiments',
                        ('experiment', dict(name='toytown', repetitions=1, runMetricsEveryStep='false'), [
                                ('setup', 'setup pathshow-setup'),
                                ('go', 'model-step'),
                                ('final', 'write-final-report'),
                                ('exitCondition', 'ticks > end-time'),
                                ('metric', '(list end-time vehicles-drowned vehicles-diverted vehicles-isolated)'),
                                values('start-time', ["7:45"]),
                                values('Scenario', [Scenario]),
                                values('heuristic-factor', [1.25]),
                                values('log-interval', ["2m"]),
                                values('end-time-str', ["8:20"]),
                                values('random-seed', [seedcount]),
                                ]))]

def runheadless(nlogofile, xmlfile):
	args = ['java',
		'-Xmx1024M',  '-cp', NetLogoJar,
		'org.nlogo.headless.HeadlessWorkspace',
                '--table', 'table-output.csv',
		'--model', nlogofile, 
		'--setup-file', xmlfile, 
		'--experiment', 'toytown']
	p = subprocess.Popen(args)
	p.wait()
	print "done with %s" % str(p.returncode)



for d in defences:
        for wt in warningtime:
                for sc in seed:
                        for sl in sealevel:
                                for pp in percentages:
                                        
                                        print "Running for defence " + d.upper() + " failure, storm surge " + str(sl) + "m, evacuation time " + wt + " random seed " + str(sc) + " proportion " + str(pp*100)
                                        writenetlogofile('timeline.txt', timeline(d, wt, sl ) )
                                        writexmlfile('behaviour.xml', behaviourxml(sc) )
                                        ##writexmlfile('behaviour.xml', behaviour, s )
                                        'kk'
                                        writenetlogofile('vehicles.txt', vehicletype(pp))
                                        runheadless('toytown.nlogo', Scenario + '/behaviour.xml')
                                        r = Scenario + '/result-breach-' + d + '-' + str(sl) + '-' + ''.join(wt.split(':')) + '-' + str(sc) + '-' + str(pp*100)
                                        shutil.rmtree(r, True)
                                        os.mkdir(r)
                                        for f in glob.glob(Scenario + "/*.out"):
                                                shutil.copy(f, r)


##for wt in warningtime:
##        defences = ["f"]
##	print "running for warning time " + wt
##	writenetlogofile('timeline.txt', warningtime(wt))
##	runheadless('toytown.nlogo', Scenario + '/behaviour.xml')
##	r = Scenario + '/result-warningtime-' + wt
##	shutil.rmtree(r, True)
##	os.mkdir(r)
##	for f in glob.glob(Scenario + "/*.out"):
##		shutil.copy(f, r)		
