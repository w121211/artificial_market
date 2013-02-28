# clean up
rm(list=ls())

# gloabl setting
ROOT = "~/git/artificial_market/log/"
#ROOT = "/Volumes/WD-PASSPORT/log/"
INDEX = "5"
SIMULATION_RUN = 100
TRAINING_T = 1000

CRASH_PECT = 0.04  # crash percentage
CRASH_STEP = 5     # crashes insde the step

# functions
logReturn <- function(p) {
  r <- rep(NA, length(p)-1)
  for (i in 1 : length(r)) {
    r[i] = log(p[i+1] / p[i])
  }
  return (r)
}

ratioReturn <- function(p) {
  r <- rep(NA, length(p)-1)
  for (i in 1 : length(r)) {
    r[i] = (p[i+1]-p[i]) / p[i]
  }
  return (r)
}