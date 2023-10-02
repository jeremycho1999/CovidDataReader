# COVID Data Reader

## Overview

The COVID Data Reader is a data analysis tool that utilizes a three-tier architecture to ingest property data, population data, and COVID-19 data from various sources and provides a variety of insights and analytics. This project aims to help users gain valuable insights into the impact of COVID-19 on different regions, considering factors like population density and property characteristics.

## Three-Tier Architecture

The COVID Data Reader is structured into three tiers:

1. **Data Ingestion Tier**: Responsible for fetching and collecting data from CSV files. This tier includes data sources for property data, population data, and COVID-19 data.

2. **Data Processing Tier**: Handles data cleaning, transformation, and calculations. It prepares the ingested data for analysis by combining and structuring it appropriately.

3. **Data Analysis and Insights Tier**: This tier leverages the processed data to generate insights. Users can interact with the system to retrieve valuable information related to COVID-19, population demographics, and property characteristics.

## Features

- **Data Ingestion**: Automatically ingests CSV data around properties, population, and COVID-19 metrics.

- **Data Processing**: Cleans and processes the data for the end user to pull.

- **Analytics**: Provides a framework and rudimentary UI for the user to glean a variety of analytics and insights, including:
  - COVID-19 infection rates by region
  - Demographic analysis of COVID-19 impact
  - Property-related insights, such as the effect of property density on infection rates

