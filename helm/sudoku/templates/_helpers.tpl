{{/* 
Expand the name of the chart, provide default or use nameOverride. 
Use required for better error handling if no value is provided. 
*/}}
{{- define "sudoku.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" | required "name is required" }}
{{- end }}

{{/*
Create a default fully qualified app name. We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name, it will be used as a full name.
*/}}
{{- define "sudoku.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "sudoku.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "sudoku.labels" -}}
helm.sh/chart: {{ include "sudoku.chart" . }}
{{ include "sudoku.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service | required "Release service is required" }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "sudoku.selectorLabels" -}}
app.kubernetes.io/name: {{ include "sudoku.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "sudoku.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "sudoku.fullname" .) .Values.serviceAccount.name | required "service account name is required" }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}
