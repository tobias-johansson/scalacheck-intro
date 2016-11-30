#!/bin/bash


amm -p '
import $ivy.`org.scalacheck::scalacheck:1.13.4`
import org.scalacheck._
import org.scalacheck.Prop.forAll
'
