clear
format long
earthRadiusInMeters = 6371000;
% Coordinates of  A:
LatA = 48.8674546593467;
LonA = 2.355035664087364;
% Coordinates of  B: 
LatB = 48.86649526947721;
LonB = 2.355244287413478;
% Coordinates of  C:
LatC = 48.8670763004335;
LonC = 2.356347186129253;
% Theoretical coordinates of the Blind Node
LatD_th = 48.86691223210363;
LonD_th = 2.355337952126204;
% Distances measured Blind node to anchor nodes [km]
DistA = 64.2402 / 1000;
DistB = 46.8676 / 1000;
DistC = 76.0414 / 1000;

[LatD, LonD] = intersectCircles(LatA, LonA, DistA, LatB, LonB, DistB, LatC, LonC, DistC);

[LatD_th, LonD_th]
[LatD, LonD]


function [lat, lon] = intersectCircles( LatA, LonA, DistA, LatB, LonB, DistB, LatC, LonC, DistC )
% intersectCircles: returns the coordinates of a point over the distance to 3 reference points
% Provide the coordinates [decimal degrees] of the reference points,
% and distances [km] to reference points,
% to obtain the coordinates on the terrestrial globe modeled by an authalic sphere.
%
% Example:
% long format
% [lat, lon] = intersectCircles (37.418436, -121.963477, 0.265710701754, 37.417243, -121.961889, 0.234592423446, 37.418692, -121.960194, 0.0548954278262)
% lat =
% 37.419102373825389
% lon =
% -1.219605792083924e + 02
%
% assuming elevation = 0
earthR = 6371;

% Convert geodetic Lat/Long to ECEF xyz
%   1. Convert Lat/Long to radians
%   2. Convert Lat/Long(radians) to ECEF
xA = earthR *(cos(deg2rad(LatA)) * cos(deg2rad(LonA)));
yA = earthR *(cos(deg2rad(LatA)) * sin(deg2rad(LonA)));
zA = earthR *(sin(deg2rad(LatA)));
xB = earthR *(cos(deg2rad(LatB)) * cos(deg2rad(LonB)));
yB = earthR *(cos(deg2rad(LatB)) * sin(deg2rad(LonB)));
zB = earthR *(sin(deg2rad(LatB)));
xC = earthR *(cos(deg2rad(LatC)) * cos(deg2rad(LonC)));
yC = earthR *(cos(deg2rad(LatC)) * sin(deg2rad(LonC)));
zC = earthR *(sin(deg2rad(LatC)));
P1 = [xA; yA; zA];
P2 = [xB; yB; zB];
P3 = [xC; yC; zC];
%% Transformation

% transform to get circle 1 at origin
% transform to get circle 2 on x axis
ex = (P2 - P1) / (norm(P2 - P1));
i  = dot(ex, (P3 - P1));
ey = (P3 - P1 - i*ex) / (norm(P3 - P1 - i*ex));
ez = cross(ex, ey);
d  = norm(P2 - P1);
j  = dot(ey, (P3 - P1));
%% Estimation
x = ((DistA^2) - (DistB^2) + (d^2))/(2*d);
y = (((DistA^2) - (DistC^2) + (i^2) + (j^2))/(2*j)) - ((i/j)*x);
% only one case shown here
z = sqrt((DistA^2) - (x^2) - (y^2));

triPt = P1 + x*ex + y*ey + z*ez;
%% Conversion finale
% convert back to lat/long to degrees
lat = rad2deg(asin(triPt(3) / earthR));
lon = rad2deg(atan2(triPt(2),triPt(1)));
end
